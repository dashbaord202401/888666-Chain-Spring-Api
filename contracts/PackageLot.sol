// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract PackageLot is Base {
    constructor() Base("PackageLot", "PKLOT") {}

    function transferFrom(
        address from,
        address to,
        uint256 tokenID
    ) public override(ERC721, IERC721) {
        tradeManagement._beforePackageLotTransfer(from, to, tokenID);
        super.transferFrom(from, to, tokenID);
    }

    function transfer(address to, uint256 tokenID) public {
        transferFrom(ownerOf(tokenID), to, tokenID);
    }

    // 铸币批次NFT
    function mint(
        string memory tokenURI,
        string memory name,
        uint256[] memory _childIDs
    ) external override (Base) returns (uint256) {
        uint256 ts = totalSupply() + 1;
        // 设置URI
        _setTokenURI(ts, tokenURI);
        // 发放代币
        _safeMint(msg.sender, ts);
        // 包装批次
        tradeManagement.checkInAndBelongPackagesMapping(msg.sender, _childIDs);
        tradeManagement.LinkParentNFT2Packages(ts, name, _childIDs);
        return ts;
    }

}
