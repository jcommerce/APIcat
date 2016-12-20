import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {Router} from "@angular/router";

@Component({
  selector: 'definition-add',
  templateUrl: './definition-add.component.html',
  styleUrls: ['./definition-add.component.scss'],

})
export class DefinitionAddComponent implements OnInit {

  definition: Definition = new Definition();

  constructor(private definitionService: DefinitionService, private router: Router) {
  }

  ngOnInit(): void {
  }

  onSubmit(definition: Definition): void {
    this.definitionService.create(definition)
      .then(definition =>
        this.router.navigate(['/definitions', definition.id])
      );
  }
}
