import ExcelJS from "exceljs";
import { saveAs } from "file-saver";

export const exportToExcel = async (jsonData, fileName) => {
  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet("Failed Records");

  const headers = Object.keys(jsonData[0]);
  worksheet.addRow(headers);

  jsonData.forEach((rowData) => {
    worksheet.addRow(Object.values(rowData));
  });

  worksheet.getRow(1).eachCell((cell) => {
    cell.fill = {
      type: "pattern",
      pattern: "solid",
      fgColor: { argb: "FF87CEEB" },
    };
    cell.font = {
      bold: true,
      color: { argb: "FF000000" },
    };
  });

  worksheet.columns.forEach((column) => {
    let maxLength = 10;
    column.eachCell({ includeEmpty: true }, (cell) => {
      const value = cell.value ? cell.value.toString() : "";
      maxLength = Math.max(maxLength, value.length + 2);
    });
    column.width = maxLength;
  });

  const buffer = await workbook.xlsx.writeBuffer();
  const blob = new Blob([buffer], {
    type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
  });
  saveAs(blob, fileName);
};
