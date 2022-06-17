const TronWeb = require('tronweb');

const fullNode = 'https://api.nileex.io/';
const solidityNode = 'https://api.nileex.io';
const eventServer = 'https://event.nileex.io/';
const privateKey = '64cdf3b85c6582090504aaba927d5f2039271de4547cb00947a3fb873337079e';

let tronWeb = new TronWeb(
    fullNode,
    solidityNode,
    eventServer,
    privateKey
);




module.exports = tronWeb;