#!/usr/bin/env bash

# Parse command line arguments as key-value pairs
while [[ $# -gt 0 ]]; do
    case "$1" in
        --projectDir)
            project_dir="$2"
            shift 2
            ;;
        --packageName)
            package_name="$2"
            shift 2
            ;;
        --agentName)
            agent_name="$2"
            shift 2
            ;;
        --steps)
            steps="$2"
            shift 2
            ;;
        --projectName)
            project_name="$2"
            shift 2
            ;;
        *)
            echo "Invalid option: $1"
            exit 1
            ;;
    esac
done

# Check if all required parameters are provided
if [ -z "$project_dir" ] || [ -z "$package_name" ] || [ -z "$agent_name" ] || [ -z "$steps" ] || [ -z "$project_name" ]; then
    echo "Usage: $0 --projectDir <project_directory> --packageName <package_name> --agentName <agent_name> --steps <steps> --projectName <project_name>"
    exit 1
fi

# Execute jar with parameters
java -jar ./build/libs/lmos-cli.jar --dirName "$project_dir" --agentName "$agent_name" --packageName "$package_name" --steps "$steps" --projectName "$project_name"

echo -e "\033[1m                    GENERATED NEW PROJECT: $project_name  \033[0m"

new_proj_dir="${project_dir}/${project_name}"

# Change directory to project directory
cd "$new_proj_dir" || { echo "Failed to change directory"; exit 1; }

echo "changed the working directory to ${PWD}"
echo "new proj name is: $new_proj_dir , and pwd is ${PWD}"

echo -e "\033[1m                    BUILDING PROJECT: $project_name \033[0m"

# Build project using gradle
./gradlew clean build

cd "build/libs" || { echo "Failed to change to build directory for generated project"; exit 1; }

echo -e "\033[1m                    STARTING APPLICATION: $project_name \033[0m"
# Execute "$project_name.jar"
java -jar "$project_name.jar"

echo "Script execution complete!"
