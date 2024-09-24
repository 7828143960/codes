package opstree.common

import opstree.common.*

def dependency_scanning_factory(Map step_params) {
    logger = new logger()
    if (step_params.dependency_check == 'true') {
        dependency_scan(step_params)
    }
  else {
        logger.logger('msg':'No valid option selected for dependency scanning. Please mention correct values.', 'level':'WARN')
  }
}

def dependency_scan(Map step_params) {
    logger = new logger()
    parser = new parser()
    dependency_check_reports = new reports_management()

    logger.logger('msg':'Performing Dependency Check Scanning', 'level':'INFO')

    repo_url = "${step_params.repo_url}"
    repo_url_type = "${step_params.repo_url_type}"

    dependency_check = "${step_params.dependency_check}"
    dependency_scan_tool = "${step_params.dependency_scan_tool}"
    fail_job_if_dependency_returned_exception = "${step_params.fail_job_if_dependency_returned_exception}"

    owasp_project_name = "${step_params.owasp_project_name}"
    owasp_report_format = "${step_params.owasp_report_format}"
    owasp_report_publish = "${step_params.owasp_report_publish}"
    owasp_version = '8.2.1'
    owasp_directory = "${HOME}/OWASP-Dependency-Check"
    owasp_project = "dependency-check scan: \$(pwd)"
    owasp_data_directory = "${owasp_directory}/data"
    owasp_cache_directory = "${owasp_directory}/data/cache"
    source_code_path = "${step_params.source_code_path}"
    app_stack = "${step_params.app_stack}"
    pom_location = "${step_params.pom_location}"

    repo_dir = parser.fetch_git_repo_name('repo_url':"${repo_url}")
    new_repo_dir = repo_dir + source_code_path

    if (dependency_scan_tool == 'owasp') {
        sh "mkdir -p ${WORKSPACE}/owasp-reports"
        sh "mkdir -p ${JENKINS_HOME}/owasp-data/cache"

            // This below line is temp fix.. Need to check alternative

        sh "sudo chmod 777 -R ${WORKSPACE}/owasp-reports ${JENKINS_HOME}/owasp-data"

        if (fail_job_if_dependency_returned_exception == true) {
            try {
                if (app_stack == 'java') {
                    sh "docker run --rm -v ${WORKSPACE}/'${pom_location}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }
                else if (app_stack == 'python' || app_stack == 'angular') {
                    sh "docker run --rm -v ${WORKSPACE}/'${repo_dir}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --enableExperimental --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }

                else {
                    sh "docker run --rm -v ${WORKSPACE}/'${repo_dir}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }
            }

                catch (Exception e) {
                logger.logger('msg':"Dependency Scanning Failed: Error Details: ${e}", 'level':'ERROR')
                }
        }

        else {
            try {
                if (app_stack == 'java') {
                    sh "docker run --rm -v ${WORKSPACE}/'${pom_location}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }

                else if (app_stack == 'python' || app_stack == 'angular') {
                    sh "docker run --rm -v ${WORKSPACE}/'${repo_dir}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --enableExperimental --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }
                
                else {
                    sh "docker run --rm -v ${WORKSPACE}/'${repo_dir}':/src:z -v ${WORKSPACE}/owasp-reports:/reports:z -v ${JENKINS_HOME}/owasp-data:/usr/share/dependency-check/data:z owasp/dependency-check:${owasp_version} --scan /src --format ALL --project '${owasp_project_name}' --out /reports"
                }
            }
                catch (Exception e) {
                logger.logger('msg':"Dependency Scanning Failed: [IGNORING] ${e}", 'level':'WARN')
                }
        }

        if (owasp_report_publish == 'true') {
            logger.logger('msg':'Publishing OWASP Dependency Scan Report', 'level':'INFO')
            dependency_check_reports.publish('dc_publisher':'true', 'report_dir':"${WORKSPACE}/owasp-reports", 'report_file':"dependency-check-report.${owasp_report_format}", 'report_name':'OWASP Dependency Check Report')
        }

            else {
            logger.logger('msg':'OWASP Report Publishing Skipped', 'level':'INFO')
            }
    }

        else if (dependency_scan_tool == 'fossa') {
        echo 'Fossa will be added soon'
        }

        else {
        logger.logger('msg':"No valid option was selected for scanning tool. Error Details: ${e}", 'level':'ERROR')
        }
}
