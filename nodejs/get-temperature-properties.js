const evtRequest = require('./evt-request');
const _ = require('lodash');

/**
 *
 * @param {{device: String, option: String, value: String}} triggerFields
 * @return {Promise.<Array>}
 */
module.exports = async function fetchPropertyUpdates(triggerFields) {
  const { device } = triggerFields;
  const uri = `thngs/${device}/properties/temperature`;

  const propertiesPromise = evtRequest(uri);
  const thngPromise = evtRequest(`thngs/${device}`);

  const [properties, thng] = await Promise.all([propertiesPromise, thngPromise]);

  return _.map(properties, propertyData => _.assign(
    {
      device: thng.name,
      meta: {
        id: propertyData.timestamp.toString(),
        timestamp: propertyData.timestamp
      }
    },
    propertyData
  ));
};
