const axios = require('axios');


class APIService {
  constructor() {}
  getJsonData(fileName) {
    return axios.get("http://localhost:8080/files/json/" + fileName);
  }

}

module.exports = new APIService();