// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./@openzeppelin/contracts/token/ERC721/IERC721.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Burnable.sol";
import "./@openzeppelin/contracts/access/Ownable.sol";
import "./TradeManagement.sol";

contract Base is ERC721Enumerable, ERC721URIStorage, Ownable {
    // 记录TradeManagement合约地址
    TradeManagement public tradeManagement;

    constructor(string memory name_, string memory symbol_)
    ERC721(name_, symbol_)
    Ownable(msg.sender)
    {}

    // 设置TradeManagement合约地址
    function setTradeManagementSC(address _trademgmtsc) external {
        tradeManagement = TradeManagement(_trademgmtsc);
    }

    // 获取指定地址的代币
    function getTokensFromOwner(address owner)
    external
    view
    returns (uint256[] memory tokens)
    {
        uint256 count = balanceOf(owner);
        tokens = new uint256[](count);
        for (uint256 i = 0; i < count; i++) {
            tokens[i] = tokenOfOwnerByIndex(owner, i);
        }
        return tokens;
    }

    // 获取指定地址以及授权管理的代币
    function getTokensFromOwnerAndAuth(address owner, address[] memory auths)
    external
    view
    returns (uint256[] memory tokens)
    {
        uint256 count = balanceOf(owner);
        for (uint256 i = 0; i < auths.length; i++) {
            count += balanceOf(auths[i]);
        }
        tokens = new uint256[](count);

        return tokens;
    }

    // 获取所有代币
    function getTokens()
    external
    view
    returns (uint256[] memory tokens)
    {
        uint256 count = totalSupply();
        tokens = new uint256[](count);
        for (uint256 i = 0; i < count; i++) {
            tokens[i] = tokenByIndex(i);
        }
        return tokens;
    }

    modifier onlyTM() {
        require(
            msg.sender == address(tradeManagement),
            "Only the Trade Management smart contract can run this function"
        );
        _;
    }

    /** 默认继承父类 **/
    function _increaseBalance(address account, uint128 value)
    internal
    virtual
    override(ERC721, ERC721Enumerable)
    {
        super._increaseBalance(account, value);
    }

    function _update(
        address to,
        uint256 tokenId,
        address auth
    ) internal virtual override(ERC721, ERC721Enumerable) returns (address) {
        return super._update(to, tokenId, auth);
    }

    // 判断是否支持指定接口
    function supportsInterface(bytes4 interfaceId)
    public
    view
    virtual
    override(ERC721URIStorage, ERC721Enumerable)
    returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }

    function tokenURI(uint256 tokenID)
    public
    view
    virtual
    override(ERC721, ERC721URIStorage)
    returns (string memory)
    {
        return super.tokenURI(tokenID);
    }

}
