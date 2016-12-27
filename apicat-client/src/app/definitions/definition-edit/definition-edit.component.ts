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
      .switchMap((params: Params) => this.definitionService.getDefinition(+params['id']))
      .subscribe(definition => {
        this.definition = definition;
        this.loadingService.hideSpinner()
      });
  }

  onSubmit(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService.update(definition)
      .subscribe(
        definition => {
          this.loadingService.hideSpinner();
          this.router.navigate(['/definitions', definition.id]);
        },
        error => this.alertMessageService.showErrorMessage("Unable to update definition. Error:" + error)
      );
  }
}
