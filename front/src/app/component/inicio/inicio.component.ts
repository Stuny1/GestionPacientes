import { Component, OnInit } from "@angular/core";
import { ChartOptions, ChartType, ChartDataSets } from "chart.js";
import * as pluginDataLabels from "chartjs-plugin-datalabels";
import { Label } from "ng2-charts";
import { CommonModule, formatDate } from "@angular/common";
import { StatisticsService } from "../../services/statistics.service";
import { AuthenticationService } from "../../shared/authentication-service";
import { Router } from '@angular/router';


@Component({
  selector: "app-inicio",
  templateUrl: "./inicio.component.html",
  styleUrls: ["./inicio.component.scss"],
})
export class InicioComponent implements OnInit {
  public columns2 = [
    { name: "First Name" },
    { name: "Last Name" },
    { name: "Address" },
  ];
  public Data2 = [
    { firstName: "Daenarys", lastName: "Targaryen", address: "Dragonstone" },
    { firstName: "Sansa", lastName: "Stark", address: "Winterfell" },
    { firstName: "Cersei", lastName: "Lannister", address: "Kings Landing" },
    { firstName: "Brienne", lastName: "Tarth", address: "Sapphire Island" },
    { firstName: "Lyanna", lastName: "Mormont", address: "Bear Island" },
    { firstName: "Margaery", lastName: "Tyrell", address: "Highgarden" },
  ];
  public bestChiropractor = "";
  public services = ["", "Espalda", "Cuello", "Cadera", "Cadera", ""];
  public columns = [];
  public tableData = [];
  public diasLabel: Label[] = [];
  public pacientesChartData: ChartDataSets[] = [
    { data: [], label: "Numero Pacientes" },
  ];
  public change = 0;
  public barChartOptions: ChartOptions = {
    responsive: true,
    // We use these empty structures as placeholders for dynamic theming.
    scales: { xAxes: [{}], yAxes: [{}] },
    plugins: {
      datalabels: {
        anchor: "end",
        align: "end",
      },
    },
  };
  public barChartType: ChartType = "bar";
  public barChartLegend = true;
  public barChartPlugins = [pluginDataLabels];

  constructor(public statics: StatisticsService,public authService: AuthenticationService,private router:Router) {

  }

  ngOnInit() {

    this.statics.getAllStatics().subscribe((data: any) => {
      console.log(data);

      for (let elem of data) {
        if (elem.category == "patients") {
          for (let point of elem.points) {
            this.diasLabel.push("Dia " + point.x);
            this.pacientesChartData[0].data.push(point.y);
          }
        }
        if (elem.category == "services") {
          let insertData = {};
          let i = 0;
          for (let point of elem.points) {
            let fecha = new Date(point.x);
            let columncont =
              fecha.getUTCFullYear() +
              "-" +
              (fecha.getMonth() + 1) +
              "-" +
              fecha.getUTCDate();
            this.columns = this.columns.concat({
              name: columncont,
              prop: columncont,
            });
            insertData[columncont] = this.services[point.y];
          }

          this.tableData = this.tableData.concat(insertData);
        }

        if (elem.category == "chiropractors") {
          this.bestChiropractor = elem.value;
        }
      }
    });
  }

  // events
  public chartClicked({
    event,
    active,
  }: {
    event: MouseEvent;
    active: {}[];
  }): void {
    console.log(event, active);
  }

  public chartHovered({
    event,
    active,
  }: {
    event: MouseEvent;
    active: {}[];
  }): void {
    console.log(event, active);
  }
}
