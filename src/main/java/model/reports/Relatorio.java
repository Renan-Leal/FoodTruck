package model.reports;

import java.io.IOException;
import java.util.HashMap;

import model.dao.Banco;
import model.vo.VendaVO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

public class Relatorio {

	public void gerarRelatorioListaPedidos() {
		try {
			String currentPath = "";
			try {
				currentPath = new java.io.File(".").getCanonicalPath();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
			System.out.println(currentPath);
			JasperRunManager.runReportToPdfFile(currentPath + "/RelatorioListaPedidos.jasper", currentPath + "/RelatorioListaPedidos.pdf", null,
					Banco.getConnection());
			System.out.println("Relatorio gerado em " + currentPath + "/RelatorioListaPedidos.pdf");
		} catch (JRException ex) {
			System.out.println("Não foi possivel imprimir, por favor verifique o modelo de impressão");
		}

	}
	
	public void gerarRelatorioEntrega(VendaVO vendaVO) {
		try {
			String currentPath = "";
			try {
				currentPath = new java.io.File(".").getCanonicalPath();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
			System.out.println(currentPath + " - " + vendaVO.getIdVenda());
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("IDVENDA", vendaVO.getIdVenda());
			JasperRunManager.runReportToPdfFile(currentPath + "/RelatorioEntrega.jasper", currentPath + "/RelatorioEntrega.pdf", parameters,
					Banco.getConnection());
			System.out.println("Relatorio gerado em " + currentPath + "/RelatorioEntrega.pdf");
		} catch (JRException ex) {
			System.out.println("Não foi possivel imprimir, por favor verifique o modelo de impressão");
		}

	}

}
