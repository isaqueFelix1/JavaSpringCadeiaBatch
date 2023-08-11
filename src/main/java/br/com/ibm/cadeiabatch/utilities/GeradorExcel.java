package br.com.ibm.cadeiabatch.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.ibm.cadeiabatch.entity.ExtratoHoras;
import br.com.ibm.cadeiabatch.entity.LogOut;
import br.com.ibm.cadeiabatch.enums.CategoriaEnum;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;

public class GeradorExcel {

	public FileOutputStream gerarExcelWorkHours(List<ExtratoHoras> extracao, boolean consolidado) {
		
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
	//Prepara planilha	
	HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet firstSheet = workbook.createSheet("Aba1");
	
	//File file = new File("C:\\ExtracaoWorkHours");
	//if(!file.exists()) {
	//	file.mkdir();
	//}
	
	FileOutputStream fos = null;	
	ByteArrayOutputStream bos = new ByteArrayOutputStream();    
	
	try {		
						
		fos = new FileOutputStream("ExtratoWorkHours.xls");

		int i = 0;
		
		//HEADER DA PLANILHA		
		HSSFRow row = firstSheet.createRow(i++);
		
		if(consolidado) {
			row.createCell(0).setCellValue("NOME");
			row.createCell(1).setCellValue("DATA");
			row.createCell(2).setCellValue("HORAS");
		}else {
		
			row.createCell(0).setCellValue("CHAMADO");
			row.createCell(1).setCellValue("DESCRIÇÃO");
			row.createCell(2).setCellValue("TIPO");
			row.createCell(3).setCellValue("CATEGORIA");
			row.createCell(4).setCellValue("DATA");
			row.createCell(5).setCellValue("MES");
			row.createCell(6).setCellValue("HORAS");
			row.createCell(7).setCellValue("NOME");
			row.createCell(8).setCellValue("NOME_EMPRESA");
		}
		
		for (ExtratoHoras extratoHoras : extracao) {
		
			//LINHAS DA PLANILHA
			row = firstSheet.createRow(i++);
			
			if(consolidado) {
				row.createCell(0).setCellValue(extratoHoras.getNome());
				row.createCell(1).setCellValue(dateFormat.format(extratoHoras.getData()));
				row.createCell(2).setCellValue(String.format(extratoHoras.getHoras().toString()).replace(".", ","));
			}else {
						
				row.createCell(0).setCellValue(String.format(extratoHoras.getNumero().toString()));
				row.createCell(1).setCellValue(extratoHoras.getDescricao());
				row.createCell(2).setCellValue(String.format(TipoChamadoEnum.values()[extratoHoras.getTipo()].toString()));
				row.createCell(3).setCellValue(String.format(CategoriaEnum.values()[extratoHoras.getCategoria()].toString()));
				row.createCell(4).setCellValue(dateFormat.format(extratoHoras.getData()));
				row.createCell(5).setCellValue(extratoHoras.getMes());
				row.createCell(6).setCellValue(String.format(extratoHoras.getHoras().toString()).replace(".", ","));
				row.createCell(7).setCellValue(extratoHoras.getNome());
				row.createCell(8).setCellValue(extratoHoras.getNome_empresa());
			}				
			
		} // fim do for
		
		workbook.write(fos);		
		bos.writeTo(fos);
		workbook.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("Erro ao exportar arquivo");
	} finally {
		try {						
			fos.flush();
			fos.close();
			bos.close();
			
		} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	return fos;
	} // fim do metodo exp	
	
	public byte[] gerarExcelJournalBatch(List<LogOut> logsRet) {		
			
		//Prepara planilha	
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet firstSheet = workbook.createSheet("Aba1");
		
		File file = new File("C:\\ExtracaoJournalBatch");
		if(!file.exists()) {
			file.mkdir();
		}
		
		FileOutputStream fos = null;	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();    
		
		try {		
							
			fos = new FileOutputStream(file + "\\ExtratoJournalBatch.xls" );

			int i = 0;
			
			//HEADER DA PLANILHA		
			HSSFRow row = firstSheet.createRow(i++);
			
			row.createCell(0).setCellValue("CHAMADO");
			row.createCell(1).setCellValue("JOB");
			row.createCell(2).setCellValue("NIVEL");
			row.createCell(3).setCellValue("FRENTE");
			row.createCell(4).setCellValue("USUARIO");
			row.createCell(5).setCellValue("DATA");
			row.createCell(6).setCellValue("DESCRICAO");			
			
			for (LogOut logs : logsRet) {
			
				//LINHAS DA PLANILHA
				row = firstSheet.createRow(i++);
				
				row.createCell(0).setCellValue(String.format("%d", logs.getIncidente()));
				row.createCell(1).setCellValue(String.format("%s", logs.getjob()));
				row.createCell(2).setCellValue(String.format("%s", logs.getNivel().name()));
				row.createCell(3).setCellValue(String.format("%s", logs.getFrente()));
				row.createCell(4).setCellValue(String.format("%s", logs.getUsuario()));
				row.createCell(5).setCellValue(String.format("%s", logs.getDataHoraCriacaoFormatado()));
				row.createCell(6).setCellValue(String.format("%s", logs.getDescricao()));
				
			} // fim do for
			
			workbook.write(fos);		
			bos.writeTo(fos);
			workbook.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao exportar arquivo");
		} finally {
			try {						
				fos.flush();
				fos.close();
				bos.close();
				
			} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		return bos.toByteArray();
		} // fim do metodo exp	
	
}
