// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract ProductLot is Base {
    constructor() Base("ProductLot", "PDLOT") {}

    // 铸币批次NFT
    function mint(
        string memory tokenURI,
        string memory name,
        uint256[] memory childIDs
    ) external override (Base) returns (uint256) {
        uint256 ts = totalSupply() + 1;
        // 设置URI
        _setTokenURI(ts, tokenURI);
        // 发放代币
        _safeMint(msg.sender, ts);
        // 生产批次
        tradeManagement.LinkParentNFT2Products(ts, childIDs);
        return ts;
    }

}
