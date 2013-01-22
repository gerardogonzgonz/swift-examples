package com.abiquo.swift;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.domain.Location;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class SwiftExample
{
    // private static String url = "http://www.apache.org/";
    // private static String url = "http://10.60.21.187/server/messagebroker/amf";
    // private static String url = "http://10.60.21.187/api/login";

    public static void main(final String[] args) throws IOException
    {
        try
        {
            String provider = "swift";
            // String identity = "account1:admintest";
            String identity = "geraradmin:geraradmin";
            String credential = "admin";
            // String credential = "21232f297a57a5a743894a0e4a801fc3";

            String containerName = "TESTINGCONTAINER";

            // String endpoint = "http://10.60.1.62:8080/auth";
            String endpoint = "http://192.168.1.105:8080/auth";
            BlobStoreContext context =
                ContextBuilder.newBuilder(provider).credentials(identity, credential)
                    .endpoint(endpoint).apiVersion("1.0")
                    .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule()))
                    .buildView(BlobStoreContext.class);

            BlobStore blobStore = context.getBlobStore();

            // {providerMetadata={id=swift, name=OpenStack Swift with SwiftAuth, api={id=swift,
            // name=OpenStack Swift with SwiftAuth, views=[org.jclouds.blobstore.BlobStoreContext],
            // endpointName=https endpoint, identityName=tenantId:user,
            // credentialName=Optional.of(password), documentation=http://api.openstack.org/,
            // api=interface org.jclouds.openstack.swift.SwiftClient, asyncApi=interface
            // org.jclouds.openstack.swift.SwiftAsyncClient},
            // endpoint=http://192.168.1.105:8080/auth, console=Optional.absent(),
            // homepage=Optional.absent(), linkedServices=[swift], iso3166Codes=[]},
            // identity=geraradmin:geraradmin}

            // {providerMetadata={id=swift, name=OpenStack Swift Pre-Diablo API, api={id=swift,
            // name=OpenStack Swift Pre-Diablo API, views=[org.jclouds.blobstore.BlobStoreContext],
            // endpointName=https endpoint, identityName=tenantId:user,
            // credentialName=Optional.of(password), documentation=http://api.openstack.org/,
            // api=interface org.jclouds.openstack.swift.SwiftClient, asyncApi=interface
            // org.jclouds.openstack.swift.SwiftAsyncClient},
            // endpoint=http://192.168.1.105:8080/auth, console=Optional.absent(),
            // homepage=Optional.absent(), linkedServices=[swift], iso3166Codes=[]},
            // identity=geraradmin:geraradmin}

            Set< ? extends Location> locations = blobStore.listAssignableLocations();
            System.out.println("Starting with locations");
            for (Location n : locations)
            {
                System.out.println(n);
            }
            // createContainer(blobStore, containerName, null);
            // listContainersAndBlobCount(context, blobStore);
            // deleteContainer(containerName, blobStore);

            // createContainer(blobStore, containerName, null);
            // uploadBlob(containerName, blobStore);
            // Blob blob = blobStore.getBlob(containerName, "objectloaded");
            // InputStream in = blob.getPayload().getInput();
            // copy(in, System.out);
            // System.out.flush();
            // blobStore.removeBlob(containerName, "objectloaded");
            // System.out.println("\nblob " + blobStore.blobExists(containerName, "objectloaded"));

            System.out.println("FINISH!!!");

            context.close();
        }
        finally
        {

        }
    }

    public static long copy(final InputStream from, final OutputStream to) throws IOException
    {
        byte[] buf = new byte[0x1000]; // 4KB
        long total = 0;
        while (true)
        {
            int r = from.read(buf);
            if (r == -1)
            {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static void listContainersAndBlobCount(final BlobStoreContext context,
        final BlobStore blobStore)
    {
        for (StorageMetadata resourceMd : blobStore.list())
        {
            if (resourceMd.getType() == StorageType.CONTAINER
                || resourceMd.getType() == StorageType.FOLDER)
            {
                // Use Map API
                Map<String, InputStream> containerMap =
                    context.createInputStreamMap(resourceMd.getName());
                System.out.printf("  %s: %s entries%n", resourceMd.getName(), containerMap.size());
            }
        }
    }

    public static void uploadBlob(final String containerName, final BlobStore blobStore)
    {
        Blob blob = blobStore.blobBuilder("objectloaded").payload(new File("pom.xml")).build();
        blobStore.putBlob(containerName, blob);
    }

    public static void createContainer(final BlobStore blobStore, final String containerName,
        final Location location)
    {
        blobStore.createContainerInLocation(location, containerName);
    }

    public static void deleteContainer(final String containerName, final BlobStore blobStore)
    {
        blobStore.deleteContainer(containerName);
    }
}
