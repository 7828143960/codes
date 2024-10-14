package opstree.golang

import opstree.common.*

def build_factory(Map step_params) {
    logger = new logger()
    if (step_params.perform_code_build == 'true') {
        build_artifact(step_params)
    }
    else {
        logger.logger('msg':'No valid option selected for Building Artifact. Please mention correct values.', 'level':'WARN')
    }
}

def build_artifact(Map step_params) {
    logger = new logger()
    parser = new parser()

    logger.logger('msg':'Performing Build Step for Go', 'level':'INFO')

    repo_url = "${step_params.repo_url}"
    source_code_path = "${step_params.source_code_path}"

    repo_dir = parser.fetch_git_repo_name('repo_url':"${repo_url}")
    // Construct the full path to the source code directory
    def repo_dir_full = "${WORKSPACE}/${repo_dir}${source_code_path}"

    dir("${WORKSPACE}/${repo_dir}") {
        // Directly pass the command to the Docker container
        sh """ docker run --rm -v ~/.go:/go -v ${repo_dir_full}:/app -w /app golang:1.19 sh -c "go mod tidy && go build -o ${step_params.output_binary}" """
        logger.logger('msg':'Build successful', 'level':'INFO')
    }
}
