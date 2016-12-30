import {InMemoryDbService} from 'angular-in-memory-web-api';

export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const sampleSwaggerDefinition = '---\r\nswagger: \'2.0\'\r\ninfo:\r\n  version: 1.0.0\r\n  title: ' +
      'Echo\r\nschemes:\r\n  - http\r\nhost: mazimi-prod.apigee.net\r\nbasePath: /echo\r\npaths:\r\n  /:\r\n    ' +
      'get:\r\n      responses:\r\n        200:\r\n          description: Echo GET';

    let formats = [
      'SWAGGER',
      'APIARY',
      'RAML'
    ];

    let specificationStages = [
      'DRAFT',
      'RELEASED'
    ];

    let definitions = [
      {
        id: 1,
        name: 'apicat api',
        version: '2.3.5',
        author: 'John Doe',
        format: formats[0],
        content: sampleSwaggerDefinition
      },
      {
        id: 2,
        name: 'Untitled rest api',
        version: '21',
        author: 'Joe Doe',
        format: formats[0],
        content: sampleSwaggerDefinition
      }
    ];

    let specifications = [
      {
        id: 45,
        name: 'First api specification',
        version: '1.1',
        author: 'John Doe',
        format: formats[0],
        stage: specificationStages[0],
        content: sampleSwaggerDefinition
      },
      {
        id: 67,
        name: 'Second api specification',
        version: '2016.1.1',
        author: 'Joe Doe',
        format: formats[0],
        stage: specificationStages[1],
        content: sampleSwaggerDefinition
      }
    ];

    return {
      definitions: definitions,
      formats: formats,
      specifications: specifications,
      specificationStages: specificationStages
    };
  }
}
