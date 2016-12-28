import {Component, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {ApiFormatService} from "../../common/api-format.service";
import {Observable} from "rxjs";
import {Specification} from "../../model/specification";

@Component({
  selector: 'specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.scss'],

})
export class SpecificationFormComponent implements OnInit {

  @Input() specification: Specification;
  @Output('submitSpecification') submitEmitter = new EventEmitter<Specification>();

  formats: Observable<string[]>;

  constructor(private apiFormatService: ApiFormatService) {
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
  }

  onSubmit(): void {
    this.submitEmitter.emit(this.specification);
  }
}
