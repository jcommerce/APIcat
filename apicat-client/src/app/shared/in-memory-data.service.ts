import { InMemoryDbService } from 'angular-in-memory-web-api';

export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    let definitions = [
      {
        id: 1,
        name: 'apicat api',
        version: '2.3.5',
        author: 'John Doe',
        format: 'SWAGGER',
        content: "---\r\nswagger: '2.0'\r\ninfo:\r\n  version: 1.0.0\r\n  title: Echo\r\nschemes:\r\n  - http\r\nhost: mazimi-prod.apigee.net\r\nbasePath: /echo\r\npaths:\r\n  /:\r\n    get:\r\n      responses:\r\n        200:\r\n          description: Echo GET"
      },
      {
        id: 2,
        name: 'Untitled rest api',
        version: '21',
        author: 'Joe Doe',
        format: 'SWAGGER',
        content: "---\r\nswagger: '2.0'\r\ninfo:\r\n  version: 1.0.0\r\n  title: Echo\r\nschemes:\r\n  - http\r\nhost: mazimi-prod.apigee.net\r\nbasePath: /echo\r\npaths:\r\n  /:\r\n    get:\r\n      responses:\r\n        200:\r\n          description: Echo GET"
      }
    ];

    return {definitions};
  }
}
