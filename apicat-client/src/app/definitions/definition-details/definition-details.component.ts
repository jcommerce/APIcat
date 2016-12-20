import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'definition-details',
  templateUrl: './definition-details.component.html',
  styleUrls: ['./definition-details.component.scss'],

})
export class DefinitionDetailsComponent implements OnInit {
  definition: Definition;

  constructor(private definitionService: DefinitionService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.definitionService.getDefinition(+params['id']))
      .subscribe(definition => this.definition = definition);
  }

}
