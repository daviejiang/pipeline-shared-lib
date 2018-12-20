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

        stage("Deployment"){
                echo "Deploy to QA..."
        }

        stage("Verify"){
                echo "Verify..."
        }

        stage("Deploy to PROD(grey)"){
                echo "Publish artifacts..."
                echo FileHelper.hello('Jiang')
        }

        stage("")
}
