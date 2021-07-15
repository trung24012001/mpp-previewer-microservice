const axios = require('axios');


class APIService {
  constructor() {}
  getJsonData(fileName) {
    return axios.get("http://0.0.0.0:8080/files/json/" + fileName);
  }

}

module.exports = new APIService();