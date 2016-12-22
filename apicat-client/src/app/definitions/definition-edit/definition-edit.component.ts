import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {Router, Params, ActivatedRoute} from "@angular/router";

@Component({
  selector: 'definition-edit',
  templateUrl: './definition-edit.component.html',
  styleUrls: ['./definition-edit.component.scss'],

})
export class DefinitionEditComponent implements OnInit {

  definition: Definition;

  constructor(private definitionService: DefinitionService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.definitionService.getDefinition(+params['id']))
      .subscribe(definition => this.definition = definition);
  }

  onSubmit(definition: Definition): void {
    this.definitionService.update(definition)
      .subscribe(definition => this.router.navigate(['/definitions', definition.id]));
  }
}
