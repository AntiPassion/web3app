// SPDX-License-Identifier: GPL-3.0
pragma solidity >0.6.12;


contract demo01 {

    function svalue(address payable addr)public payable{
        addr.transfer(msg.value);
    }
    
    function transderToContract() payable public {
        payable(address(this)).transfer(msg.value);
    }

    function getBalanceOfContract() public view returns (uint256) {
        return address(this).balance;
    }
    
    
    receive() external payable {}
    fallback() external payable{}
}
