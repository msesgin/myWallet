# myWallet

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./gradlew
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.

## Building for production

To optimize the myWallet application for production, run:

    ./gradlew -Pprod clean bootRepackage

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar build/libs/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./gradlew test    

[Using JHipster in development]: https://jhipster.github.io/documentation-archive/v4.4.1/development/
[Service Discovery and Configuration with the JHipster-Registry]: https://jhipster.github.io/documentation-archive/v4.4.1/microservices-architecture/#jhipster-registry
[Using JHipster in production]: https://jhipster.github.io/documentation-archive/v4.4.1/production/
[Running tests page]: https://jhipster.github.io/documentation-archive/v4.4.1/running-tests/

[Gatling]: http://gatling.io/
[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[BrowserSync]: http://www.browsersync.io/
