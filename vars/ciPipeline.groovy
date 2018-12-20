import com.example.qa.FileHelper
import hudson.model.Result

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

        stage("Checkout"){
                echo "Checkout source code..."
        }

        stage("Build&Test"){
                echo "Build package..."
        }

        stage("Publish"){
                echo "Publish artifacts..."
                FileHelper.hello('Jiang')
        }
}
