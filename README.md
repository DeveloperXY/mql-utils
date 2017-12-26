[![Build Status](https://travis-ci.org/DeveloperXY/mql-utils.svg?branch=master)](https://travis-ci.org/DeveloperXY/mql-utils)

# mql-utils 
A Java library that offers compile-time checking for the best practices taught by Mr. Noureddine CHENFOUR, in addition to other utility classes & annotations.

# How to use

1. Create a `package-info.java` file anywhere in your project
2. Annotate the package declaration with the `@CheckForBestPractices` annotation, which is required to trigger the checks. This will result into a basic class names' checking, but you can further customize the process using the API's annotations.

# Annotations supported so far:

`@Action`,
`@Model`,
`@Dao`,
`@Service`,
`@NameSuffixedWith`
