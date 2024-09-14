# Rail Madad

Enhanced version of IRCTC © Rail Madad, with automated Complaint Management using Batch Processing Services and Keyword
Analysis.

## Setup Instructions

### Pre-requisites

1. Git
2. Docker Engine `v24.0.6` and above
3. Java JDK `22.0.2`
4. IntelliJ IDEA `v2024.1.6` and above
5. Plugin `google-java-format v1.23.0.0` and above
6. **Optional:** Postman `v11.12.0` and above
7. **Optional:** Plugin `JPA Buddy 241.18968.26` and above

### Steps

1. Ensure that the Docker Engine is running and healthy.
2. Either use IntelliJ IDEA's `Run` button or execute the following command in the terminal: `.\mvnw spring-boot:run`
3. You may use Postman/curl/`.http` files to test the endpoints.

### Code Push & Workflows

1. Ensure that `google-java-format` is enabled for the project. You can enable it from:
   `Settings > Plugins > google-java-format`. Use the Default Style.
2. **Optional but Recommended:** Ensure that Reformat Code, Optimize Imports & Rearrange Code are enabled
   for `Actions on Save`
3. **Optional but Recommended:** Ensure that Reformat Code, Optimize Imports & Rearrange Code are enabled for `Settings`
   Menu in the `Commit` Panel. This will ensure that these requirements are met before pushing a commit.
4. Ensure you have formatted all code files.
5. Run the linter: `.\mvnw checkstyle:check`, and ensure it is passing.
6. Run the tests: `.\mvnw clean test`, and ensure that all tests are passing.
7. Create a new branch with the naming convention of: `feature-name`, and `checkout`.
8. Commit your changes to your branch.
9. Push your changes and the branch to remote.
10. On GitHub, create a new Pull Request.
11. Ensure that all status checks are passing and there are no merge conflicts.

The `Format, Lint & Test Java files` workflow does the following for every PR into `main`:

- Check the formatting of `.java` files and fail if differences are found.
- Check the code style using Google's standard Java checkstyle and fail if warnings are encountered.
- Test the SpringBoot Application and fail if 1 or more tests fail.

**Force Pushes and Commits to main are not allowed, and will be rejected by GitHub.**

Every PR into `main` must have at least `1` approved review before merging. The merged branch will be deleted hereafter.
Please fetch `main` again.
