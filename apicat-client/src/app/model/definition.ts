import {CrudEntity} from './crud-entity';

export class Definition extends CrudEntity {
  name: string;
  version: string;
  author: string;
  format: string;
  content: string;
}
