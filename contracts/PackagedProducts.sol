// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "./Base.sol";

contract PackagedProducts is Base {
    // 构造器
    constructor() Base("PackagedProducts", "PP") {}

    /** 业务逻辑 **/

    // 重写交易逻辑
    function transferFrom(
        address from,
        address to,
        uint256 tokenID
    ) public override (ERC721, IERC721) {
        require(
            tradeManagement.recordInnerTransfer(to, tokenID),
            "fail to record transfer"
        );
        super.transferFrom(from, to, tokenID);
    }

    function transfer(address to, uint256 tokenID) public {
        transferFrom(ownerOf(tokenID), to, tokenID);
    }

    function transferBatch (address to, uint256 tokenID, uint num) public {
        for (uint i = 0; i < num; i++) {
            transferFrom(ownerOf(tokenID + i), to, tokenID + i);
        }
    }

    function burn (uint256[] memory tokenIDs, string memory finalName) public {
        for (uint i = 0; i < tokenIDs.length; i++) {
            transferFrom(ownerOf(tokenIDs[i]), address(tradeManagement), tokenIDs[i]);
            tradeManagement.burnPackagedProduct(tokenIDs[i], finalName);
        }
    }

    // 铸币
    function mint(
        uint256 numberOfTokens,
        string memory _tokenURI,
        string memory name,
        uint256 productLot,
        uint produceTime
    ) public payable returns (uint256[] memory tokens) {
        tradeManagement.onlyProducer(msg.sender);
        uint256 ts = totalSupply() + 1;
        tokens = new uint256[](numberOfTokens);
        // 购买代币
        for (uint256 i = 0; i < numberOfTokens; i++) {
            // 设置URI
            _setTokenURI(ts + i, _tokenURI);
            // 发放代币
            _safeMint(msg.sender, ts + i);
            tokens[i] = ts + i;
        }

        tradeManagement.InitPackagedProductsNFT(
            msg.sender,
            tokens,
            name,
            productLot,
            produceTime
        );

        return tokens;
    }

    function approvalTMForAll (bool tag) public {
        setApprovalForAll(address(tradeManagement), tag);
    }

}
