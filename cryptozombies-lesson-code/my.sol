// SPDX-License-Identifier: GPL-3.0
pragma solidity >0.6.12;


contract Demo01 {

    uint[] public numbers;

    function test01() public pure returns(uint32){
        return 2**6;
    }

    function setNumbers() public {
        numbers.push(5);
        numbers.push(10);
        numbers.push(15);
    }

    struct Person {
        uint age;
        string name;
    }
    Person[] public people;

    function setPerson() public {
        // Person memory zs = Person(10,"zhangsan");
        // people.push(zs);
        people.push(Person(10,"zhangsan"));
    }

    uint8 a=10;
    uint b = 20;
    uint8 c = a * uint8(b);
    function test02() public view returns(uint8) {
        return c;
    }

    function test03(string memory _str) public pure returns(uint) {
        return uint(keccak256(bytes(_str)));
    }

    function _generateRandomDna(string memory _str) private pure returns (uint) {
        uint rand = uint(keccak256(bytes(_str)));
        return rand;
    }
}
