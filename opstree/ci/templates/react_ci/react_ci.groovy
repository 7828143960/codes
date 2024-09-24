package opstree.ci.templates.react_ci

import opstree.common.*
import opstree.react.*

def call(Map step_params) {
    ansiColor('xterm') {
        workspace = new workspace_management()
        vcs = new git_management()
        credscan = new vulnerability_scanning()
        dependencyscan = new dependency_scanning()
        static_code_analysis = new static_code_analysis()
        securityscan = new snyk_scanning()
        build_dockerfile = new build_dockerfile()
        publish = new publish_artifact()
        trivy = new docker_image_scanning()
        image_size_validator = new image_size_validator()
        notify = new notify()
        build = new build_artifact()

        if (step_params.repo_url_type == 'http') {
            repo_url = "${step_params.repo_https_url}"
        }
        else if (step_params.repo_url_type == 'ssh') {
            repo_url = "${step_params.repo_ssh_url}"
        }
        
        try {
            stage('Workspace Management') {
                workspace.workspace_management(
                    clean_workspace: "${step_params.clean_workspace}",
                    ignore_clean_workspace_failure: "${step_params.ignore_clean_workspace_failure}",
                    delete_dirs: "${step_params.delete_dirs}",
                    clean_when_build_aborted: "${step_params.clean_when_build_aborted}",
                    clean_when_build_failed: "${step_params.clean_when_build_failed}",
                    clean_when_not_built: "${step_params.clean_when_not_built}",
                    clean_when_build_succeed: "${step_params.clean_when_build_succeed}",
                    clean_when_build_unstable: "${step_params.clean_when_build_unstable}"
                )
            }

            stage('Git Checkout') {
                vcs.git_checkout(
                    repo_url: "${repo_url}",
                    repo_branch: "${step_params.repo_branch}",
                    clean_workspace: "${step_params.clean_workspace}",
                    repo_url_type: "${step_params.repo_url_type}",
                    ssh_private_key_location: "${step_params.ssh_private_key_location}",
                    jenkins_git_ssh_key_id: "${step_params.jenkins_git_ssh_key_id}",
                    jenkins_git_creds_id: "${step_params.jenkins_git_creds_id}",
                    source_code_path: "${step_params.source_code_path}"
                )
            }

            stage('Pre-Build Checks') {
                def tasks = [:]

                if (step_params.gitleaks_check != null && step_params.gitleaks_check.toBoolean()) {
                    tasks['GitleaksCredsScanning'] = {
                        credscan.creds_scanning_factory(
                            gitleaks_check: "${step_params.gitleaks_check}",
                            repo_url: "${repo_url}",
                            repo_url_type: "${step_params.repo_url_type}",
                            gitleaks_report_format: "${step_params.gitleaks_report_format}",
                            gitleaks_report_jenkins_publish: "${step_params.gitleaks_report_jenkins_publish}",
                            fail_job_if_leak_detected: "${step_params.fail_job_if_leak_detected}"
                        )
                    }
                }

                if (step_params.dependency_check != null && step_params.dependency_check.toBoolean()) {
                    tasks['OWASPCodeDepedencyScanning'] = {
                        dependencyscan.dependency_scanning_factory(
                            repo_url: "${repo_url}",
                            repo_url_type: "${step_params.repo_url_type}",
                            owasp_project_name: "${step_params.owasp_project_name}",
                            owasp_report_publish: "${step_params.owasp_report_publish}",
                            owasp_report_format: "${step_params.owasp_report_format}",
                            dependency_check: "${step_params.dependency_check}",
                            dependency_scan_tool: "${step_params.dependency_scan_tool}",
                            fail_job_if_dependency_returned_exception: "${step_params.fail_job_if_dependency_returned_exception}",
                            source_code_path: "${step_params.source_code_path}"
                        )
                    }
                }

                if (tasks) {
                    parallel tasks
                } else {
                    echo 'No checks were enabled, skipping Pre-Build Checks stage.'
                }
            }

            if (step_params.perform_code_build != null && step_params.perform_code_build.toBoolean()) {
                stage('Build Artifact') {
                    build.build_factory(
                        perform_code_build: "${step_params.perform_code_build}",
                        repo_url: "${repo_url}",
                        repo_url_type: "${step_params.repo_url_type}",
                        source_code_path: "${step_params.source_code_path}"
                    )
                }
            }

            if (step_params.static_code_analysis_check != null && step_params.static_code_analysis_check.toBoolean()) {
                stage('Static Code Analysis') {
                    static_code_analysis.static_code_analysis_factory(
                        repo_url: "${repo_url}",
                        repo_url_type: "${step_params.repo_url_type}",
                        codebase_to_scan_directory: "${step_params.codebase_to_scan_directory}",
                        static_code_analysis_check: "${step_params.static_code_analysis_check}",
                        path_to_sonar_properties: "${step_params.path_to_sonar_properties}",
                        fail_job_if_analysis_returned_exception: "${step_params.fail_job_if_analysis_returned_exception}",
                        jenkins_sonarqube_token_creds_id: "${step_params.jenkins_sonarqube_token_creds_id}",
                        app_stack: "${step_params.app_stack}",
                        unit_testing_check: "${step_params.unit_testing_check}",
                        source_code_path: "${step_params.source_code_path}"
                    )
                }
            } else {
                echo 'Skipping Static Code Analysis stage as it is disabled.'
            }

            if (step_params.perform_build_dockerfile != null && step_params.perform_build_dockerfile.toBoolean()) {
                stage('Build Docker Image') {
                    build_dockerfile.build_factory(
                        perform_build_dockerfile:"${step_params.perform_build_dockerfile}",
                        repo_url: "${repo_url}",
                        image_name: "${step_params.image_name}",
                        static_code_analysis_check: "${step_params.static_code_analysis_check}",
                        app_stack: 'react',
                        source_code_path: "${step_params.source_code_path}",
                        dockerfile_context: "${step_params.dockerfile_context}",
                        dockerfile_location: "${step_params.dockerfile_location}",
                        codeartifact_dependency: "${step_params.codeartifact_dependency}",
                        codeartifact_domain: "${step_params.codeartifact_domain}",
                        codeartifact_owner: "${step_params.codeartifact_owner}"
                    )
                }
            } else {
                echo 'Skipping Build Docker Image stage as it is disabled.'
            }

            stage('Post-Build Checks') {
                def tasks = [:]

                if (step_params.image_scanning_check != null && step_params.image_scanning_check.toBoolean()) {
                    tasks['ImageScanning'] = {
                        trivy.image_scanning_factory(
                            image_scanning_check: "${step_params.image_scanning_check}",
                            image_name: "${step_params.image_name}",
                            image_tag: "${step_params.image_tag}",
                            scan_severity: "${step_params.scan_severity}",
                            image_scanning_report_publish: "${step_params.image_scanning_report_publish}"
                        )
                    }
                }

                if (step_params.image_size_validator_check != null && step_params.image_size_validator_check.toBoolean()) {
                    tasks['ImageSizeValidator'] = {
                        image_size_validator.size_validator_factory(
                            image_size_validator_check: "${step_params.image_size_validator_check}",
                            image_name: "${step_params.image_name}",
                            image_tag: "${step_params.image_tag}",
                            max_allowed_image_size: "${step_params.max_allowed_image_size}",
                            fail_job_if_validation_fail: "${step_params.fail_job_if_validation_fail}"
                        )
                    }
                }

                if (tasks) {
                    parallel tasks
                } else {
                    echo 'No image scanning or validation tasks are enabled, skipping this stage.'
                }
            }

            if (step_params.artifact_publish_check != null && step_params.artifact_publish_check.toBoolean()) {
                stage('Publish Artifact') {
                    publish.publish_factory(
                        repo_url: "${repo_url}",
                        artifact_publish_check: "${step_params.artifact_publish_check}",
                        artifact_destination_type: "${step_params.artifact_destination_type}",
                        jenkins_aws_credentials_id: "${step_params.jenkins_aws_credentials_id}",
                        docker_image_name: "${step_params.docker_image_name}",
                        ecr_repo_name: "${step_params.ecr_repo_name}",
                        ecr_region: "${step_params.ecr_region}",
                        account_id: "${step_params.account_id}"
                    )
                }
            } else {
                echo 'Skipping Publish Artifact stage as it is disabled.'
            }
        } catch (Exception e) {
            currentBuild.result = 'FAILURE'
            if (step_params.notification_enabled.toBoolean()) {
                notify.notification_factory(
                    build_status: 'FAILURE',
                    notify_channel: "${step_params.notify_channel}",
                    notify_tool: "${step_params.notify_tool}",
                    notify_tool_type: "${step_params.notify_tool_type}",
                    notify_channel_creds_id: "${step_params.notify_channel_creds_id}"
                )
            }
            throw e
        } finally {
            if (step_params.notification_enabled.toBoolean()) {
                notify.notification_factory(
                    build_status: currentBuild.result,
                    notify_channel: "${step_params.notify_channel}",
                    notify_tool: "${step_params.notify_tool}",
                    notify_tool_type: "${step_params.notify_tool_type}",
                    notify_channel_creds_id: "${step_params.notify_channel_creds_id}"
                )
            }
        }
    }
}
