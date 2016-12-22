import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";

@Component({
  selector: 'definition-list',
  templateUrl: './definition-list.component.html',
  styleUrls: ['./definition-list.component.scss'],

})
export class DefinitionListComponent implements OnInit {
  definitions: Definition[];

  constructor(private definitionService: DefinitionService) {
  }

  ngOnInit(): void {
    this.getDefinitions();
  }

  getDefinitions(): void {
    this.definitionService.getDefinitions()
      .then(definitions => this.definitions = definitions);
  }

  delete(definition: Definition): void {
    this.definitionService
      .delete(definition.id)
      .then(() => this.definitions = this.definitions.filter(d => d !== definition));
  }
}
