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

        echo "Show environments"
        echo env.getEnvironment().toString()

        baStage("Checkout"){
                echo "Checkout source code..."
        }

        baStage('Unit tests') {
                ut.parse()
        }

        baStage("Build&Test"){
                echo "Build package..."
        }

        baStage("Publish"){
                echo "Publish artifacts..."
                echo FileHelper.hello('Jiang')
        }
}
