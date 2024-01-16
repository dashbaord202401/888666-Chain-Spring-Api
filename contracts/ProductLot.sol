// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract ProductLot is Base {
    constructor() Base("ProductLot", "PDLOT") {}

    // 铸币批次NFT
    function mint(
        string memory tokenURI,
        string memory name,
        string memory lotName,
        uint256 produceTime,
        uint256[] memory childIDs,
        uint128[] memory resumes
    ) external returns (uint256) {
        uint256 ts = totalSupply() + 1;
        // 验证是否拥有权限消耗原料,即是原料的拥有者
        tradeManagement.checkRawMaterialsOwner(msg.sender, childIDs);
        // 验证是否消耗量足够
        tradeManagement.checkRawMaterialsTotalSum(childIDs, resumes);
        // 设置URI
        _setTokenURI(ts, tokenURI);
        // 发放代币
        _safeMint(msg.sender, ts);
        // 生产批次
        tradeManagement.InitProductNFT(ts, msg.sender, name, lotName, produceTime, childIDs, resumes);
        return ts;
    }

}
