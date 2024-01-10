// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

contract Tool {
    constructor() {}

    function getAddress() public view returns (address) {
        return address(msg.sender);
    }

}
