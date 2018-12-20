/**
 * Extended baStage block
 * @param stageName
 * @param config
 * @param body
 * @return
 */

def call(String stageName, Map config =[:], Closure body) {
        if (!stageName) {
            error "stage name is required..."
        }

        echo "Config:"
        echo config.toString()

        stage(stageName){
            body()
        }
}