import {Component, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {Definition} from "../../model/definition";
import {ApiFormatService} from "../../common/api-format.service";
import {Observable} from "rxjs";
import {ApiFormat} from "../../model/api-format";

@Component({
  selector: 'definition-form',
  templateUrl: './definition-form.component.html',
  styleUrls: ['./definition-form.component.scss'],

})
export class DefinitionFormComponent implements OnInit {

  @Input() definition: Definition;
  @Output('submitDefinition') submitEmitter = new EventEmitter<Definition>();

  formats: Observable<ApiFormat[]>;

  constructor(private apiFormatService: ApiFormatService) {
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
  }

  onSubmit(): void {
    this.submitEmitter.emit(this.definition);
  }
}
