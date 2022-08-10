// SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.7.0;

contract transferTest { 
    
    function transderToAddress(address _add, uint256 _val) payable public {
        payable(_add).transfer(_val);
    }

    // 向合约账户转账   
    function transderToContract() payable public {
        payable(address(this)).transfer(msg.value);
    }
    
    // 获取合约账户余额 
    function getBalanceOfContract() public view returns (uint256) {
        return address(this).balance;
    }
    
    fallback() external payable {}
    
    receive() external payable {}

}