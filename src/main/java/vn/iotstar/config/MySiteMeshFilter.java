package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter{

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		// TODO Auto-generated method stub
		builder.addDecoratorPath("/*", "/web.jsp")
				.addDecoratorPath("/admin/*", "/admin.jsp")
				.addExcludedPath("/v1/api/*");
	}
}
