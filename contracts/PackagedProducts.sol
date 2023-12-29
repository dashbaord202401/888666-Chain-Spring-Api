// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "./@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "./@openzeppelin/contracts/access/Ownable.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";
import "./@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "./TradeManagement.sol";

contract PackagedProducts is
ERC721,
ERC721URIStorage,
ERC721Enumerable,
Ownable
{
    // 记录TradeManagement合约地址
    TradeManagement public tradeManagement;

    // 构造器
    constructor() ERC721("PackagedProducts", "PP") Ownable(msg.sender) {}

    // 设置TradeManagement合约地址
    function SetTradeManagementSC(address _trademgmtsc) external {
        tradeManagement = TradeManagement(_trademgmtsc);
    }

    /** 权限判断 **/

    /** 业务逻辑 **/

    // 在代币转移前执行
    function _beforeTokenTransfer(
        address to,
        uint256 tokenID
    ) internal {
        require(
            tradeManagement.recordInnerTransfer(to, tokenID),
            "TODO"
        );
    }

    // 重写交易逻辑
    function transferFrom(
        address from,
        address to,
        uint256 tokenID
    ) public override (ERC721, IERC721) {
        _beforeTokenTransfer(to, tokenID);
        super.transferFrom(from, to, tokenID);
    }

    function transfer(
        address to,
        uint256 tokenID
    ) public {
        transferFrom(msg.sender, to, tokenID);
    }

    // 铸币
    function mint(
        uint256 numberOfTokens,
        string memory _tokenURI,
        string memory name,
        string memory productLot,
        uint256[] memory _childIDs,
        uint128[] memory resumes
    ) public payable returns (uint256[] memory tokens) {
        tradeManagement.onlyProducer(msg.sender);
        uint256 ts = totalSupply() + 1;
        // 验证是否拥有权限消耗原料,即是原料的拥有者
        tradeManagement.checkRawMaterialsOwner(msg.sender, _childIDs);
        // 验证是否消耗量足够
        tradeManagement.checkRawMaterialsTotalSum(
            _childIDs,
            resumes
        );
        tokens = new uint256[](numberOfTokens);
        // 购买代币
        for (uint256 i = 0; i < numberOfTokens; i++) {
            // 设置URI
            _setTokenURI(ts + i, _tokenURI);
            // 发放代币
            _safeMint(msg.sender, ts + i);
            tokens[i] = ts + i;
        }

        tradeManagement.InitPackagedProductsNFT(msg.sender, tokens, name, productLot, _childIDs, resumes);

        return tokens;
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
    override(ERC721, ERC721URIStorage, ERC721Enumerable)
    returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }

    // 返回URI
    function tokenURI(uint256 tokenID)
    public
    view
    virtual
    override(ERC721, ERC721URIStorage)
    returns (string memory)
    {
        return super.tokenURI(tokenID);
    }

    function getAddress() public view returns (address) {
        return address(this);
    }

    function approvalTMForAll(bool approved) public virtual {
        super.setApprovalForAll(address(tradeManagement), approved);
    }
}
