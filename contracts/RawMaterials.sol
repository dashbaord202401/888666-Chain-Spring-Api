// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract RawMaterials is Base {
    constructor() Base("RawMaterials", "RM") {}

    // 将原料从供应商转给生产商
    function transferFrom(
        address from,
        address to,
        uint256 tokenID
    ) public override (ERC721, IERC721) {
        // 验证用户身份
        tradeManagement.onlySupplier(from);
        tradeManagement.onlyProducer(to);
        // 记录
        tradeManagement.recordRawMaterialsTransfer(from, to, tokenID);
        super.transferFrom(from, to, tokenID);
    }

    function transfer(
        address to,
        uint256 tokenID
    ) public {
        transferFrom(msg.sender, to, tokenID);
    }

    // 供应商铸造原材料NFT并记录其URI
    function mint(string memory _tokenURI, uint128 initSum, string memory name)
    external
    returns (uint256)
    {
        tradeManagement.onlySupplier(msg.sender);

        uint256 ts = totalSupply() + 1;

        // 铸造NFT并记录其URI
        _safeMint(msg.sender, ts);
        _setTokenURI(ts, _tokenURI);

        // 初始化原料NFT
        tradeManagement.InitRawMaterialsNFT(msg.sender, ts, initSum, name);

        // 返回NFT的ID
        return (ts);
    }
}
