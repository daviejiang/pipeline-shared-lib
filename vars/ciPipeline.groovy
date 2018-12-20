import com.example.qa.FileHelper

/**
 * ci pipeline serves as pre-commit jobs and release jobs
 */
def call(body) {
        final String DISABLED = "DISABLED"
        final String ENABLED = "ENABLED"
        def defaultConfig = [sonarCheck: ENABLED]
        def config = defaultConfig
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        //Job parameters
        addParameters {
                rotator = logRotator(daysToKeepStr: '20', numToKeepStr: '100')
                parameters = []
                concurrent = false
        }

        echo "Show environments"
        echo env.getEnvironment().toString()

        baStage("Checkout"){
                echo "Checkout source code..."
        }

        baStage('Unit tests') {
                echo "Unit tests..."
        }

        baStage("Build&Test"){
                echo "Build package..."
        }

        baStage("Publish"){
                echo "Publish artifacts..."
        }
}