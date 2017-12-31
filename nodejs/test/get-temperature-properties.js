const getTemperatureProperties = require('../get-temperature-properties');
const nock = require('nock');
const { assert } = require('chai');
const _ = require('lodash');

describe('Get temperature properties', () => {

  it('should return trigger data', async () => {
    const pathName = 'thngs/1';
    const property = 'temperature';
    const now = Date.now();
    const mockProperties = [
      {
        key: 'temperature',
        value: 5,
        timestamp: now,
        createdAt: now,
      },
      {
        key: 'temperature',
        value: 10,
        timestamp: now + 1,
        createdAt: now + 1,
      },
      {
        key: 'temperature',
        value: 15,
        timestamp: now + 2,
        createdAt: now + 2,
      },
    ];
    const mockThng = {
      id: '1',
      name: 'test thng',
    };
    nock('https://api.evrythng.com')
    .get(`/${pathName}/properties/${property}`)
    .reply(200, mockProperties);

    nock('https://api.evrythng.com')
    .get(`/${pathName}`)
    .reply(200, mockThng);

    const triggerFields = {
      device: '1',
    };
    const propertyUpdates = await getTemperatureProperties(triggerFields);
    const expectedResult = _.map(propertyUpdates, propertyData => _.assign(
      {
        device: '1',
        meta: {
          id: propertyData.timestamp.toString(),
          timestamp: propertyData.timestamp
        }
      },
      propertyData
    ));

    assert.deepEqual(propertyUpdates, expectedResult);
  })
})