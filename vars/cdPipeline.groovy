import com.example.qa.FileHelper
import hudson.model.Result

/**
 * cd pipeline
 */
def call(body) {
        final String DISABLED = "DISABLED"
        final String ENABLED = "ENABLED"
        def defaultConfig = [sonarCheck: ENABLED]
        def config = defaultConfig
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        stage("QA Deployment"){
                echo "Deploy to QA..."
        }

        stage("QA Verify"){
                echo "Verify QA environment..."
                def cont = input(
                        id: 'Proceed1',
                        message: 'Was this successful?',
                        ok: 'Submit',
                        parameters: [
                                [$class: 'BooleanParameterDefinition', defaultValue: true, description: '', name: 'Please confirm you agree with this']
                        ])

                if(cont){
                        echo "User confirmed, but we quit"
                        currentBuild.result = 'ABORTED'
                }
        }

        stage("PROD Deployment(A/B)"){
                echo "Publish artifacts..."
        }

        stage("PROD Verify(A/B)") {
                echo "Verify production environment"
                input "Ready to full deployment?"
        }

        stage("PROD Deployment") {
                echo "Deployment to production"
        }
}
