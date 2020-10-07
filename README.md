# echo5
Service virtualisation software

We do 5 things:

- recive
- transform
- forward
- transform
- respond

## Getting started

The main idea is to make a proxy with [Netty](https://netty.io/) and intercept requests.
Why netty, because we can then virtualize services accessible by any protocol.
First protocol to implement will be: HTTP.

Currently what is need to be done:

1. Flexible http scenario json format.
  * String matcher format (f.e destination, path, etc)
  * Map matcher format (f.e forms, query, headers) 
  * Malli schems for scenario validation
2. Extendable core for matchers.
3. Bootstrap Netty instance.
4. Intercept requests
  * Find matching scenario and execute it
  * If no scenario found forward request to service
5. Execute scenario.


## Have a question?

If you need any help, please visit our [Documentation][], [GitHub issues][] or [Stack Overflow][]. Feel free to file an issue if you do not manage to find any solution from the archives.

[GitHub issues]: https://github.com/smogstate/echo5/issues?q=is%3Aissue+label%3Aquestion+
[Stack Overflow]: http://stackoverflow.com/questions/tagged/echo5
[Documentation]: https://github.com/smogstate/echo5/blob/master/doc/intro.md
