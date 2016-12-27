import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {Router, Params, ActivatedRoute} from "@angular/router";
import {AlertMessageService} from "../../common/alert/alert-message.service";
import {LoadingIndicatorService} from "../../common/loading-indicator/loading-indicator.service";

@Component({
  selector: 'definition-edit',
  templateUrl: './definition-edit.component.html',
  styleUrls: ['./definition-edit.component.scss'],

})
export class DefinitionEditComponent implements OnInit {

  definition: Definition;

  constructor(private definitionService: DefinitionService,
              private router: Router,
              private route: ActivatedRoute,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  ngOnInit(): void {
    this.loadingService.showSpinner();

    this.route.params
      .switchMap((params: Params) =>
        this.definitionService.getDefinition(+params['id']).finally(() => this.loadingService.hideSpinner())
      )
      .subscribe(
        definition => this.definition = definition,
        error => this.alertMessageService.showErrorMessage("Unable to load definition. Error: " + error)
      );
  }

  onSubmit(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService.update(definition)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        definition => this.router.navigate(['/definitions', definition.id]),
        error => this.alertMessageService.showErrorMessage("Unable to update definition. Error: " + error)
      );
  }
}
