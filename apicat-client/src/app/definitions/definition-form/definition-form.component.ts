import {Component, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";

@Component({
  selector: 'definition-form',
  templateUrl: './definition-form.component.html',
  styleUrls: ['./definition-form.component.scss'],

})
export class DefinitionFormComponent implements OnInit {

  @Input() definition: Definition;
  @Output('submitDefinition') submitEmitter = new EventEmitter<Definition>();

  constructor(private definitionService: DefinitionService) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.submitEmitter.emit(this.definition);
  }
}
