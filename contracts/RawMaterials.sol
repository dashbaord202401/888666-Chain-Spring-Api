// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Burnable.sol"; //Imported in case burning NFTs is used
import "./@openzeppelin/contracts/access/Ownable.sol";
import "./@openzeppelin/contracts/token/ERC721/IERC721.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";
import "./TradeManagement.sol";

contract RawMaterials is ERC721Enumerable, ERC721URIStorage, Ownable {
    // 记录TradeManagement合约地址
    TradeManagement public tradeManagement;

    constructor() ERC721("RawMaterials", "RM") Ownable(msg.sender) {}

    // 设置TradeManagement合约地址
    function SetTradeManagementSC(address _trademgmtsc) external {
        tradeManagement = TradeManagement(_trademgmtsc);
    }

    // 将原料从供应商转给生产商
    function transferFrom(
        address from,
        address to,
        uint256 tokenID,
        string memory lotName
    ) public {
        tradeManagement.onlySupplier(from);
        tradeManagement.onlyProducer(to);
        tradeManagement.recordRawMaterialsTransfer(from, to, tokenID, lotName);
        super.transferFrom(from, to, tokenID);
    }

    function transfer(
        address to,
        uint256 tokenID,
        string memory lotName
    ) public {
        transferFrom(msg.sender, to, tokenID, lotName);
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

    function getTokensFromOwner (address owner) view external onlyTM returns (uint[] memory tokens) {
        uint count = balanceOf(owner);
        tokens = new uint256[](count);
        for (uint i = 0; i < count; i++) {
            tokens[i] = tokenOfOwnerByIndex(owner, i);
        }
        return tokens;
    }

    function getTokens () view external onlyTM returns (uint[] memory tokens) {
        uint count = totalSupply();
        tokens = new uint256[](count);
        for (uint i = 0; i < count; i++) {
            tokens[i] = tokenByIndex(i);
        }
        return tokens;
    }


    modifier onlyTM() {
        require(msg.sender == address(tradeManagement), "Not owner of this token");
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
