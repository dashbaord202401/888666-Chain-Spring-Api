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

    function burn (uint256 tokenID, string memory finalName) public {
        transferFrom(ownerOf(tokenID), address(tradeManagement), tokenID);
        tradeManagement.burnPackagedProduct(tokenID, finalName);
    }

    // 铸币
    function mint(
        uint256 numberOfTokens,
        string memory _tokenURI,
        string memory name,
        string memory productLot,
        uint produceTime,
        uint256[] memory _childIDs,
        uint128[] memory resumes
    ) public payable returns (uint256[] memory tokens) {
        tradeManagement.onlyProducer(msg.sender);
        uint256 ts = totalSupply() + 1;
        // 验证是否拥有权限消耗原料,即是原料的拥有者
        tradeManagement.checkRawMaterialsOwner(msg.sender, _childIDs);
        // 验证是否消耗量足够
        tradeManagement.checkRawMaterialsTotalSum(_childIDs, resumes);
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
            produceTime,
            _childIDs,
            resumes
        );

        return tokens;
    }

    function approvalTMForAll (bool tag) public {
        setApprovalForAll(address(tradeManagement), tag);
    }

}
