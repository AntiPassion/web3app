let instance = require("./getTronweb")


//-- 获取余额
//tronWeb.trx.getBalance(address).then(res=>{
//    console.log(res)
//});



//-- 创建一个未签名的TRX转账交易
//tronWeb.transactionBuilder.sendTrx(to,amount,from).then(res=>{
//    console.log(res)
//})


//-- 创建一个未签名的 TRC10转账交易
//tronWeb.transactionBuilder.sendToken(to,amount,tokenID,from).then(res=>{
//    console.log(res)
//})


//4156fd2d5ae75717375aac0c6ec4051fff00505886

//tronWeb.contract().at(contractAddress).then(res=>{
//    console.log(res)
//})


class Trx {
    tronWeb
    contractAddress

    constructor(tronWeb, contractAddress) {
        this.tronWeb = tronWeb
        this.contractAddress = contractAddress
    }

    async getContract() {
       return await this.tronWeb.contract().at(this.contractAddress)
    }

    async sendRawTransaction(fromAddress,privateKey,toAddress,amount,remark){
        try {
            const parameter = [
                {
                    type:'address',
                    value:toAddress
                },
                {
                    type:'uint256',
                    value:amount
                }
            ]
            const transaction = await this.tronWeb.transactionBuilder.triggerSmartContract(this.contractAddress, "transfer(address,uint256)", {},parameter,this.tronWeb.address.toHex(fromAddress))
            transaction.transaction.data = remark
            let signedTx  = await this.tronWeb.trx.sign(transaction.transaction,privateKey)
            await this.tronWeb.trx.sendRawTransaction(signedTx);
            return signedTx.txID
        }catch(e) {
            console.log(e)
        }
    }


    async triggerSmartContract() {
        try {
            let address = 'TYXxNR3UtgD3s2ADTQAhBE9AEBf7fKfe6B';
            let contract = await this.tronWeb.contract().at(this.contractAddress);
            let result = await contract.balanceOf(address).call();
            console.log('result: ', result);
        } catch(error) {
            console.error("trigger smart contract error",error)
        }
    }

    async transfer() {
        let address = 'TYXxNR3UtgD3s2ADTQAhBE9AEBf7fKfe6B';
        let to = 'TGDWFqspH3RUN62JzLtnqmHJtMgjM7S5iX';
    
        try {
            let contract = await this.tronWeb.contract().at(this.contractAddress);
            console.log(contract);
            let result = await contract.transfer(
                to, //address _to
                1000000   //amount
            ).send({
                feeLimit: 1000000
            }).then(output => {console.log('- Output:', output, '\n');});
            console.log('result: ', result);
        } catch(error) {
            console.error("trigger smart contract error",error)
        }
    }

}



const Web3 = require('web3');

(async function () {

    const web3 = new Web3(Web3.givenProvider || "http://localhost:8545");

    let address = 'TYXxNR3UtgD3s2ADTQAhBE9AEBf7fKfe6B';
    let to = 'TGDWFqspH3RUN62JzLtnqmHJtMgjM7S5iX';
    let value = "100";
    let amount = web3.utils.toWei(value,'ether');
    let from = 'TYXxNR3UtgD3s2ADTQAhBE9AEBf7fKfe6B';
    let tokenID = '1000086'
    let privateKey = '64cdf3b85c6582090504aaba927d5f2039271de4547cb00947a3fb873337079e';

    //let contractAddress = 'TEEgkVMvVoyp3gNyDE7mj3odJXKVNs1bgd';
    let contractAddress = '412ecde7f620e4106c4904308ce4a12c7703d57307';
    // console.log(instance.address.toHex(contractAddress))

    let trx = new Trx(instance,contractAddress);
    
    let a = await trx.sendRawTransaction(address, privateKey, to, amount, "helloworld")
    console.log(a)
    // trx.transfer()
})()
