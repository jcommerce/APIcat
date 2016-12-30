import {CrudEntity} from './crud-entity';

export class Specification extends CrudEntity {
  name: string;
  version: string;
  author: string;
  format: string;
  content: string;
  stage: string;
}
