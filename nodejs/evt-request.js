const r2 = require('r2');
const _ = require('lodash');

/**
 *
 * @param {String} engineUrl
 * @param {Object} reqOptions
 * @return {Promise<Object|Array>}
 */
async function sendRequest(engineUrl, reqOptions) {
  const { headers, method, body } = reqOptions;

  const response = await r2(engineUrl, _.omitBy({ headers, method, body }, _.isEmpty)).response;
  const entity = await response.json();

  if (response.status > 299) {
    throw new Error(`Got a ${response.status} from the service: ${entity.errors[0]} when doing a ${method} to the following url: ${engineUrl}`);
  }
  return entity;
}

/**
 *
 * @param {String} pathName
 * @return {String}
 */
function buildEngineUrl(pathName) {
  return `https://api.evrythng.com/${pathName}`;
}

/**
 *
 * @param {Object} body
 * @param {String} method
 * @param {Object} additionalHeaders
 * @return {{headers: Object, method: String, body: Object}}
 */
function buildRequestOptions(body, method = 'GET', additionalHeaders = {}) {
  const headers = _.assign(
    {
      'Content-Type': 'application/json',
      'Authorization': process.env.API_KEY
    },
    additionalHeaders
  );
  const jsonData = _.isEmpty(body) ? {} : JSON.stringify(body);

  return { headers, method, body: jsonData };
}

/**
 *
 * @param {String} pathName
 * @param {String} method
 * @param {Object|Array} body
 * @return {Promise.<Object|Array>}
 */
module.exports = async (
  pathName,
  method = 'GET',
  body = {},
) => {
  const engineUrl = buildEngineUrl(
    pathName,
  );

  return sendRequest(engineUrl, buildRequestOptions(body, method));
};

module.exports.sendRequest = sendRequest;
module.exports.buildEngineUrl = buildEngineUrl;
module.exports.buildRequestOptions = buildRequestOptions;
