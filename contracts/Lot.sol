// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Burnable.sol"; //Imported in case burning NFTs is used
import "./@openzeppelin/contracts/access/Ownable.sol";
import "./TradeManagement.sol";

contract Lot is ERC721Enumerable, ERC721URIStorage, Ownable {
    // 记录TradeManagement合约地址
    TradeManagement public tradeManagement;

    constructor() ERC721("Lot", "LOT") Ownable(msg.sender) {}

    mapping(address => uint256[]) PackagedLotMapping;
    uint256[] PackagedLots;

    // 批次枚举类型
    enum LotType {
        Packages,
        Products,
        RawMaterials
    }

    // 权限控制
    modifier onlyTradeManagementSmartContract() {
        require(
            msg.sender == address(tradeManagement),
            "Only the Trade Management smart contract can run this function"
        );
        _;
    }

    // 设置TradeManagement合约地址
    function SetTradeManagementSC(address _trademgmtsc) external {
        tradeManagement = TradeManagement(_trademgmtsc);
    }

    // 铸造生产批次NFT并记录其URI
    function mintForProducts(
        string memory _tokenURI,
        uint256[] memory _childIDs
    ) external onlyTradeManagementSmartContract returns (uint256) {
        return mint(_tokenURI, "", _childIDs, LotType.Products);
    }

    // 铸造包装批次NFT并记录其URI‘
    function mintForPackages(
        string memory _tokenURI,
        string memory name,
        uint256[] memory _childIDs
    ) public returns (uint256) {
        return mint(_tokenURI, name, _childIDs, LotType.Packages);
    }

    // 铸造原料批次NFT并记录其URI
    function mintForRawMaterials(
        string memory _tokenURI,
        uint256[] memory _childIDs
    ) public returns (uint256) {
        return mint(_tokenURI, "", _childIDs, LotType.RawMaterials);
    }

    function transferPackageFrom(
        address from,
        address to,
        uint256 tokenID
    ) public {
        tradeManagement._beforePackageLotTransfer(from, to, tokenID);
        super.transferFrom(from, to, tokenID);
    }

    function transferPackage (
        address to,
        uint256 tokenID
    ) public {
        transferPackageFrom(msg.sender, to, tokenID);
    }

    // 铸币批次NFT
    function mint(
        string memory _tokenURI,
        string memory name,
        uint256[] memory _childIDs,
        LotType lotType
    ) internal returns (uint256) {
        uint256 ts = totalSupply() + 1;
        // 设置URI
        _setTokenURI(ts, _tokenURI);
        // 发放代币
        _safeMint(msg.sender, ts);
        // 根据批次类型建立联系
        // 生产批次
        if (lotType == LotType.Products) {
            tradeManagement.LinkParentNFT2Products(ts, _childIDs);
            // 原料批次
        } else if (lotType == LotType.RawMaterials) {
            tradeManagement.checkInAndBelongRawMaterialsMapping(
                msg.sender,
                _childIDs
            );
            tradeManagement.LinkParentNFT2RawMaterials(ts, _childIDs);
            // 包装批次
        } else if (lotType == LotType.Packages) {
            tradeManagement.checkInAndBelongPackagesMapping(
                msg.sender,
                _childIDs
            );
            tradeManagement.LinkParentNFT2Packages(msg.sender, ts, name, _childIDs);
            PackagedLotMapping[msg.sender].push(ts);
            PackagedLots.push(ts);
        }
        return ts;
    }

    function getPKLTokensFromOwner (address owner) view external onlyTM returns (uint256[] memory tokens) {
        return PackagedLotMapping[owner];
    }

    function getPKLTokens () view external onlyTM returns (uint256[] memory tokens) {
        return PackagedLots;
    }

    function getPKLcount () public view returns (uint) {
        return PackagedLots.length;
    }

    modifier onlyTM() {
        require(msg.sender == address(tradeManagement), "Not TM");
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