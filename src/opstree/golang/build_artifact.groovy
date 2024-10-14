package opstree.golang

import opstree.common.*

def build_factory(Map step_params) {
    logger = new logger()
    if (step_params.perform_code_build == 'true') {
        build_artifact(step_params)
    } else {
        logger.logger('msg':'No valid option selected for Building Artifact. Please mention correct values.', 'level':'WARN')
    }
}

def build_artifact(Map step_params) {
    logger = new logger()
    parser = new parser()

    logger.logger('msg':'Performing Build Step for Go', 'level':'INFO')

    repo_url = "${step_params.repo_url}"
    source_code_path = "${step_params.source_code_path}"

    // Fetch the repository name using the parser and build the full path
    repo_dir = parser.fetch_git_repo_name('repo_url':"${repo_url}")
    def full_source_path = "${env.WORKSPACE}/${source_code_path}/${repo_dir}"

    // Print debug information for verification
    logger.logger('msg':"Full source path: ${full_source_path}", 'level':'DEBUG')

    dir("${full_source_path}") {
        def build_command = "go mod tidy && go build -o ${step_params.output_binary}"

        // Use the dynamically constructed full_source_path for Docker volume mounting
        sh """ docker run --rm -v ~/.go:/go -v ${full_source_path}:/app -w /app golang:1.19 sh -c "${build_command}" """
        logger.logger('msg':'Build successful', 'level':'INFO')
    }
}
