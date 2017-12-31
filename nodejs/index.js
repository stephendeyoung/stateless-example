const restify = require('restify');
const getTemperatureProperties = require('./get-temperature-properties');

const server = restify.createServer();

server.use(restify.plugins.bodyParser());
server.use(restify.plugins.queryParser());

server.post('/ifttt/v1/triggers/temperature', async (req, res, next) => {
  try {
    const { triggerFields } = req.body;
    const triggerData = await getTemperatureProperties(triggerFields);
    res.json(triggerData);
  } catch (err) {
    next(err);
  }
})

server.on('NotFound', function (req, res, err, cb) {
  // do not call res.send! you are now in an error context and are outside
  // of the normal next chain. you can log or do metrics here, and invoke
  // the callback when you're done. restify will automtically render the
  // NotFoundError as a JSON response.
  return cb();
});

server.on('InternalServer', function (req, res, err, cb) {

  return cb();
});

server.on('restifyError', function (req, res, err, cb) {
  // this listener will fire after both events above!
  // `err` here is the same as the error that was passed to the above
  // error handlers.
  console.error('There was an error: ', err)
  return cb();
});


server.listen(8080, function() {
  console.log('%s listening at %s', server.name, server.url);
});
