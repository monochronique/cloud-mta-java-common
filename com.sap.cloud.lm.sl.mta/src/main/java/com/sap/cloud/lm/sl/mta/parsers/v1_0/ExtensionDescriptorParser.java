package com.sap.cloud.lm.sl.mta.parsers.v1_0;

import static com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas.MTAEXT;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor.ExtensionDescriptorBuilder;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends ModelParser<ExtensionDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension descriptor";

    public static final String ID = "ID";
    public static final String EXT_DESCRIPTION = "ext_description";
    public static final String EXTENDS = "extends";
    public static final String PROVIDER = "ext_provider";
    public static final String TARGET_PLATFORMS = "target-platforms";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PROPERTIES = "properties";
    public static final String SCHEMA_VERSION = "_schema-version";

    protected final Set<String> usedModuleNames = new HashSet<String>();
    protected final Set<String> usedProvidedDependencyNames = new HashSet<String>();
    protected final Set<String> usedResourceNames = new HashSet<String>();

    public ExtensionDescriptorParser(Map<String, Object> source) {
        this(MTAEXT, source);
    }

    protected ExtensionDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionDescriptor parse() throws ParsingException {
        ExtensionDescriptorBuilder builder = new ExtensionDescriptorBuilder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setParentId(getParentId());
        builder.setProvider(getProvider());
        builder.setSchemaVersion(getSchemaVersion());
        builder.setDeployTargets(getTargetPLatforms());
        builder.setModules1_0(getModules1_0());
        builder.setResources1_0(getResources1_0());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getId() throws ParsingException {
        return getStringElement(ID);
    }

    protected String getDescription() throws ParsingException {
        return getStringElement(EXT_DESCRIPTION);
    }

    protected String getParentId() throws ParsingException {
        return getStringElement(EXTENDS);
    }

    protected String getProvider() throws ParsingException {
        return getStringElement(PROVIDER);
    }

    protected String getSchemaVersion() {
        return getSchemaVersion(SCHEMA_VERSION);
    }

    protected List<String> getTargetPLatforms() {
        return getListElement(TARGET_PLATFORMS);
    }

    protected List<ExtensionModule> getModules1_0() throws ParsingException {
        return getListElement(MODULES, new ListParser<ExtensionModule>() {
            @Override
            protected ExtensionModule parseItem(Map<String, Object> map) throws ParsingException {
                return getModuleParser(map).setUsedProvidedDependencyNames(usedProvidedDependencyNames)
                    .setUsedValues(usedModuleNames)
                    .parse();
            }
        });
    }

    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source);
    }

    protected List<ExtensionResource> getResources1_0() throws ParsingException {
        return getListElement(RESOURCES, new ListParser<ExtensionResource>() {
            @Override
            protected ExtensionResource parseItem(Map<String, Object> map) throws ParsingException {
                return getResourceParser(map).setUsedValues(usedResourceNames)
                    .parse();
            }
        });
    }

    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
